import { Component, OnInit } from '@angular/core';
import { SamplesFilesService } from 'app/entities/samples-files';

@Component({
    selector: 'jhi-fileupload-progress',
    templateUrl: './fileupload-progress.component.html',
    styles: []
})
export class FileuploadProgressComponent implements OnInit {
    constructor(protected samplesFilesService: SamplesFilesService) {}
    public visibleFile: boolean = false;
    public label: string = 'Subiendo Archivo ... ';
    public classBar: string = 'progress-bar-animated';

    ngOnInit() {
        this.samplesFilesService.visibleFileUpload.subscribe(isVisible => {
            if (isVisible == 1) {
                this.label = 'Uploaded file successfully...';
                this.classBar = 'bg-success';
                this.visibleFile = false;
                this.visibleFile = true;
                var _this = this;
                setTimeout(function() {
                    _this.visibleFile = false;
                }, 8000); //Despues de 5 segundos se quita el mensaje de carga
            } else {
                if (isVisible == 0) {
                    this.label = 'Uploading file ... ';
                    this.classBar = 'progress-bar-animated';
                    console.log('entro aca verdadero :', isVisible);
                    this.visibleFile = true;
                } else {
                    this.label = 'Error loading file, check file data, select valid sampling';
                    this.classBar = 'bg-danger';
                    this.visibleFile = false;
                    this.visibleFile = true;
                    var _this = this;
                    setTimeout(function() {
                        _this.visibleFile = false;
                    }, 8000); //Despues de 5 segundos se quita el mensaje de carga
                }
            }
            //this.visibleFile = isVisible;
        });
    }

    sleep(milliseconds) {
        var start = new Date().getTime();
        for (var i = 0; i < 1e7; i++) {
            if (new Date().getTime() - start > milliseconds) {
                break;
            }
        }
    }
}
