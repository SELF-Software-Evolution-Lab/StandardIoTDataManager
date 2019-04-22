import { Moment } from 'moment';

export interface ISamplesFiles {
    id?: string;
    name?: string;
    path?: string;
    contentType?: string;
    size?: number;
    state?: number;
    result?: string;
    createDateTime?: Moment;
    updateDateTime?: Moment;
    recordsProcessed?: number;
}

export class SamplesFiles implements ISamplesFiles {
    constructor(
        public id?: string,
        public name?: string,
        public path?: string,
        public contentType?: string,
        public size?: number,
        public state?: number,
        public result?: string,
        public createDateTime?: Moment,
        public updateDateTime?: Moment,
        public recordsProcessed?: number
    ) {}
}
