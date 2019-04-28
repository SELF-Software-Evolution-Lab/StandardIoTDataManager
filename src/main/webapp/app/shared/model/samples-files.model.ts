import { Moment } from 'moment';

export interface ISamplesFiles {
    file?: File;
}

export class SamplesFiles implements ISamplesFiles {
    constructor(public file?: File) {}
}
