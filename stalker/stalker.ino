#include <avr/sleep.h>
#include <avr/power.h>
#include <avr/power.h>
#include <Arduino.h>
#include <Wire.h>
#include <DS3231.h>
#include <DHT22.h>
#include <crc16.h>

//The following code is taken from sleep.h as Arduino Software v22 (avrgcc) in w32 does not have the latest sleep.h file
#define sleep_bod_disable() \
{ \
  uint8_t tempreg; \
  __asm__ __volatile__("in %[tempreg], %[mcucr]" "\n\t" \
                       "ori %[tempreg], %[bods_bodse]" "\n\t" \
                       "out %[mcucr], %[tempreg]" "\n\t" \
                       "andi %[tempreg], %[not_bodse]" "\n\t" \
                       "out %[mcucr], %[tempreg]" \
                       : [tempreg] "=&d" (tempreg) \
                       : [mcucr] "I" _SFR_IO_ADDR(MCUCR), \
                         [bods_bodse] "i" (_BV(BODS) | _BV(BODSE)), \
                         [not_bodse] "i" (~_BV(BODSE))); \
}


#define DHT22_PIN           8
#define BATT_VOLTAGE_PIN    7
#define BATT_CHARGE_PIN     6
#define SLEEP_SEC           60

DS3231 RTC;
DHT22 myDHT22(DHT22_PIN);

//static uint8_t prevSecond=0;
static DateTime interruptTime;

//Interrupt service routine for external interrupt on INT0 pin conntected to DS3231 /INT
void INT0_ISR()
{
    //Keep this as short as possible. Possibly avoid using function calls
    detachInterrupt(0);
    interruptTime = DateTime(interruptTime.get() + SLEEP_SEC);  //decide the time for next interrupt, configure next interrupt
}


void setup()
{
    // Initialize INT0 pin for accepting interrupts
    PORTD |= 0x04;
    DDRD &=~ 0x04;
    pinMode(4, INPUT);

    Serial.begin(9600);
    Wire.begin();

    RTC.begin();
    attachInterrupt(0, INT0_ISR, LOW); //Only LOW level interrupt can wake up from PWR_DOWN
    set_sleep_mode(SLEEP_MODE_PWR_DOWN);

    //Enable Interrupt
    //RTC.enableInterrupts(EveryMinute); //interrupt at  EverySecond, EveryMinute, EveryHour
    // or this
    DateTime  start = RTC.now();
    interruptTime = DateTime(start.get() + SLEEP_SEC); //Add 5 mins in seconds to start time

    analogReference(INTERNAL);
    pinMode(BATT_CHARGE_PIN, INPUT);
    pinMode(BATT_VOLTAGE_PIN, INPUT);
    analogRead(BATT_CHARGE_PIN);
}


String getTimestamp()
{
    return String(RTC.now().get());
}

String getTemperature()
{
    DHT22_ERROR_t errorCode = myDHT22.readData();
    String result;
    switch (errorCode) {
    case DHT_ERROR_NONE:
        result = String(myDHT22.getTemperatureCInt()) + String(":") + String(myDHT22.getHumidityInt());
        break;
    default:
        result = String(errorCode);
        break;
    }
    return result;
}

String getChargeStatus()
{
    unsigned char status = 0;
    unsigned int ADC6 = analogRead(6);
    if (ADC6 > 900) {
        status = 0;     //sleeping
    } else if (ADC6 > 550) {
        status = 1;     //charging
    } else if (ADC6 > 350) {
        status = 2;     //done
    } else {
        status = 3;     //error
    }
    return String(status);
}

String getVoltage()
{
    unsigned int bat_read = analogRead(BATT_VOLTAGE_PIN);
    //  (1/1024)*6=0.0064453125,
    float voltage = (float)bat_read * 0.0064453125;
    return String((int)(voltage * 100));
}

String getInternalTemperature()
{
    RTC.convertTemperature();
    return String((int)(RTC.getTemperature() * 100));
}

void loop()
{
    ////////////////////// START : Application or data logging code//////////////////////////////////
    delay(2000);

    String out =
            getTimestamp() +
            String("|") +
            getTemperature() +
            String("|") +
            getChargeStatus()+
            String("|") +
            getVoltage() +
            String("|") +
            getInternalTemperature();

    Serial.print("~");
    Serial.print(out);
    Serial.print("^");
    char buff[128] = "";
    out.toCharArray(buff, 128);
    Serial.print(calc_crc16(buff, out.length()), HEX);
    Serial.print("=");

    delay(100);

    RTC.clearINTStatus(); //This function call is  a must to bring /INT pin HIGH after an interrupt.
    RTC.enableInterrupts(interruptTime.hour(),interruptTime.minute(),interruptTime.second());    // set the interrupt at (h,m,s)
    attachInterrupt(0, INT0_ISR, LOW);  //Enable INT0 interrupt (as ISR disables interrupt). This strategy is required to handle LEVEL triggered interrupt
    ////////////////////////END : Application code ////////////////////////////////


    //\/\/\/\/\/\/\/\/\/\/\/\/Sleep Mode and Power Down routines\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
    //Power Down routines
    cli();
    sleep_enable();      // Set sleep enable bit
    sleep_bod_disable(); // Disable brown out detection during sleep. Saves more power
    sei();

    //Serial.println("\nSleeping");
    delay(15); //This delay is required to allow print to complete
    //Shut down all peripherals like ADC before sleep. Refer Atmega328 manual
    power_all_disable(); //This shuts down ADC, TWI, SPI, Timers and USART
    sleep_cpu();         // Sleep the CPU as per the mode set earlier(power down)

    //--------------------

    sleep_disable();     // Wakes up sleep and clears enable bit. Before this ISR would have executed
    power_all_enable();  //This shuts enables ADC, TWI, SPI, Timers and USART
    delay(15); //This delay is required to allow CPU to stabilize
    //Serial.println("Awake from sleep");

    //\/\/\/\/\/\/\/\/\/\/\/\/Sleep Mode and Power Saver routines\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\

}
