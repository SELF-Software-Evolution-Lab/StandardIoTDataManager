export interface ISampleFiles {
    id?: string;
    pathContentType?: string;
    path?: any;
    name?: string;
    size?: number;
}

export class SampleFiles implements ISampleFiles {
    constructor(public id?: string, public pathContentType?: string, public path?: any, public name?: string, public size?: number) {}
}
