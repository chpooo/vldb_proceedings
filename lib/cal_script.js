$(function() {
    console.log("loading page");

    //and call getCalendarLink(i, date, from, to, location, title)  for every event in json

    $.getJSON('lib/events.json', function(data) {
//        console.log(data.events)
        
        for (var i = 0;i<  data.events.length; i++) {
//            console.log("i= " + i);
            day = data.events[i].day;

            fromHour = data.events[i].fromHour;
            fromMin = data.events[i].fromMin;

            toHour = data.events[i].toHour;
            toMin = data.events[i].toMin;

            start = getDate(day, fromHour, fromMin);
//            console.log(start)
            end = getDate(day, toHour, toMin);
//            console.log(end);
            getCalendarLink(i, start, end, data.events[i].title, data.events[i].location);
        }

    });

});



function getCalendarLink(i, start, end, title, location) {
//    console.log("creating calendar link for " + i + " with info: \n" + start + " " + end + " " + title + " " + location);
event = {

        start: start,
        end: end,
        summary: title.replace(/:/g,"\":\"").replace(/;/g,"\";\"").replace(/,/g,"\",\""),
        title: title.replace(/:/g,"\":\"").replace(/;/g,"\";\"").replace(/,/g,"\",\""),
        description: title.replace(/:/g,"\":\"").replace(/;/g,"\";\"").replace(/,/g,"\",\""),
        location: location
    };
    $('#eventcal_' + i).icalendar($.extend({icons: 'lib/icalendar.png',
        compact: true,sites: ['google', 'outlook'], 
    echoUrl: 'http://keith-wood.name/iCalEcho.php'}, event));
    
$( ".icalendar_list" ).css( "display", "block" );
//$( ".icalendar_list" ).css( "padding", "10px" );
}
function getDate(day, hour, min) {
    d = day.split(" ");
//    console.log(d)
//    console.log("2013, 7, " + d[2].substring(0, d[2].length - 2) + ", " + hour + ", " + min + ", 00");
    return new Date(2013, 7, d[2].substring(0, d[2].length - 2), hour, min, 00);
}