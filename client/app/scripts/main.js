/* global aperture */

'use strict';

$(function() {
  var map = new aperture.geo.Map('#map-demo');
  map.zoomTo( 20, 0, 3 );
});