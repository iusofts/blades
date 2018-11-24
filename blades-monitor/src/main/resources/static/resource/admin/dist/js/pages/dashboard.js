/*
 * Author: Abdullah A Almsaeed
 * Date: 4 Jan 2014
 * Description:
 *      This is a demo file used only for the main dashboard (index.html)
 **/

$(function () {

  "use strict";

  /* Morris.js Charts */
  // Sales chart
  var area = new Morris.Area({
    element: 'revenue-chart',
    resize: true,
    data: [
      {y: '2017 Q3', item1: 6810, item2: 1914},
      {y: '2017 Q4', item1: 5670, item2: 4293},
      {y: '2018 Q1', item1: 4820, item2: 3795},
      {y: '2018 Q2', item1: 15073, item2: 5967},
      {y: '2018 Q3', item1: 14687, item2: 5460},
      {y: '2018 Q4', item1: 16073, item2: 6067}
    ],
    xkey: 'y',
    ykeys: ['item1', 'item2'],
    labels: ['订单系统', '产品系统'],
    lineColors: ['#a0d0e0', '#3c8dbc'],
    hideHover: 'auto'
  });

});
