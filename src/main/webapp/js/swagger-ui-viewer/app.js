var express = require('express');
var app = express();

app.use('/view', express.static(__dirname + '/../../docs'))

var server = app.listen(3000, function () {
      var host = server.address().address;
        var port = server.address().port;

          console.log('Swagger UI Viewer listening at http://%s:%s', host, port);
});
