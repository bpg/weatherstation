#!/bin/sh
# -*- sh -*-

: << =cut

=head1 NAME

uptime - Plugin to display WeatherStation measurement

=head1 NOTES

Especially the average and max values on the bigger graphs (yearly) can be interesting.

=head1 AUTHOR

Pavel Boldyrev

=head1 LICENSE

Free for All

=head1 MAGIC MARKERS

 #%# family=auto
 #%# capabilities=autoconf

=cut

if [ "$1" = "autoconf" ]; then
	echo yes 
	exit 0
fi

if [ "$1" = "config" ]; then
	echo 'graph_title Wheather Station'
	echo 'graph_args --base 1000 -l -10 --upper-limit 100'
	echo 'graph_scale no'
	echo 'graph_vlabel ...'
	echo 'graph_category other'
	echo 'temp.label temp_C'
	echo 'humid.label humid_%'
	echo 'inttemp.label int_temp_C'
	echo 'bvoltage.label bat_vol'
	echo 'bstatus.label charging'
#	echo 'bstatus.draw AREA'
	exit 0
fi

cat /var/lib/weartherstation/snapshot

exit 0