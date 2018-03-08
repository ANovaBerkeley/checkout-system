// This is a manifest file that'll be compiled into application.js, which will include all the files
// listed below.
//
// Any JavaScript/Coffee file within this directory, lib/assets/javascripts, vendor/assets/javascripts,
// or any plugin's vendor/assets/javascripts directory can be referenced here using a relative path.
//
// It's not advisable to add code directly here, but if you do, it'll appear at the bottom of the
// compiled file. JavaScript code in this file should be added after the last require_* statement.
//
// Read Sprockets README (https://github.com/rails/sprockets#sprockets-directives) for details
// about supported directives.
//
//= require jquery
//= require jquery_ujs
//= require bootstrap-sprockets
//= require_tree .
//= require select2
//= require turbolinks
//= require quagga

function order_by_occurrence(arr) {
  var counts = {};
  arr.forEach(function(value){
      if(!counts[value]) {
          counts[value] = 0;
      }
      counts[value]++;
  });

  return Object.keys(counts).sort(function(curKey,nextKey) {
      return counts[curKey] < counts[nextKey];
  });
}

function load_quagga(){
  if ($('#barcode-scanner').length > 0 && navigator.mediaDevices && typeof navigator.mediaDevices.getUserMedia === 'function') {

    var last_result = [];
    if (Quagga.initialized == undefined) {
      Quagga.onDetected(function(result) {
        console.log('hello');
        var item = document.getElementById('testtest').innerHTML;
        var last_code = result.codeResult.code;
        last_result.push(last_code);
        if (last_result.length > 20) {
          code = order_by_occurrence(last_result)[0];
          last_result = [];
          Quagga.stop();
          if (item == -1) {
            $.ajax({
              type: "POST",
              url: '/orders/scan_member',
              data: { upc: code, item_id:  item}
            });
          } else {
            $.ajax({
              type: "POST",
              url: '/barcode_order',
              data: { upc: code, item_id:  item}
            });

          }
          
        }
      });
    }

    Quagga.init({
      inputStream : {
        name : "Live",
        type : "LiveStream",
        numOfWorkers: navigator.hardwareConcurrency,
        target: document.querySelector('#barcode-scanner')  
      },
      decoder: {
          // readers : ['ean_reader','ean_8_reader','code_39_reader','code_39_vin_reader','codabar_reader','upc_reader','upc_e_reader']
          readers : ['ean_reader']
      }
    },function(err) {
        if (err) { console.log(err); return }
        Quagga.initialized = true;
        Quagga.start();
    });

  }
};

(function(){
  var notifiers, showErrorsInResponse, showFlashMessages;
  notifiers = {
    notice: 'success',
    alert: 'error',
    info: 'info'
  };
  showFlashMessages = function(jqXHR) {
    var flash;
    if (!jqXHR || !jqXHR.getResponseHeader) return;
    flash = jqXHR.getResponseHeader('X-Flash');
    flash = JSON.parse(flash);
    return _.each(flash, function(message, key) {
      return toastr[notifiers[key]](message);
    });
  };
  showErrorsInResponse = function(jqXHR) {
    var error, response;
    if (!jqXHR || !jqXHR.responseJSON || !jqXHR.responseJSON.errors) return;
    response = jqXHR.responseJSON;
    error = _.map(response.errors, function(messages, property) {
      return _.map(messages, function(x) {
        return "" + property + " " + x;
      }).join("<br />");
    });
    return toastr.error(error, "ERROR");
  };
  $(function() {
    return $(document).ajaxComplete(function(event, xhr, settings) {
      showFlashMessages(xhr);
      showErrorsInResponse(xhr);
      return xhr.responseJSON.errors;
    });
  });
  })(this)
$(document).on('turbolinks:load', load_quagga);