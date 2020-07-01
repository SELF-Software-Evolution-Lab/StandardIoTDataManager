
this.onmessage = function(e){


    if( e.data!== undefined){
        console.log( "Archivo", e.data.file);
        console.log( "sampleId", e.data.samplingId);

        /*for( var i=0;i< 10 ; i++){
            sleep(1000);
            console.log(  "Indice : ", i );
        }*/

        let result = enviarArchivo( e.data.file, e.data.samplingId );
        let _this = this;
        result.then(msg =>{
            console.log( "Mensaje" , msg);
            this.postMessage(msg);

        },error =>{
            console.log( "Mensaje Error", error);
            this.postMessage("Error worker : " +error);
        });
        //this.postMessage("cargado");
    }


    /*function sleep(milliseconds) {
        var start = new Date().getTime();
        for (var i = 0; i < 1e7; i++) {
         if ((new Date().getTime() - start) > milliseconds) {
          break;
         }
        }
       }*/

    function enviarArchivo( file, samplingId ){
        var formData = new FormData();

        formData.append('file', file);
        formData.append('samplingId', samplingId);
        console.log('formulario:');
        console.log(formData);

        return postAsync( '/api/samples-files-2' , formData);
    }

    function postAsync(url, formData) {
        return new Promise(function(resolve, reject) {
          var req = new XMLHttpRequest();
          req.open('POST', url);

          req.onload = function() {
            if (req.status == 200) { resolve(req.response); }
            else { reject(Error(req.statusText));}
          };

          req.onerror = function() {
            reject(Error("Network Error"));
          };

          req.send(formData);
        });
      };
}
