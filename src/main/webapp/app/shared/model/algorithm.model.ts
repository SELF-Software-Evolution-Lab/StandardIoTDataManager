import { Moment } from 'moment';

export interface IAlgorithm {
    id?: string;
    name?: string;
    description?: string;
    mapperText?: any;
    mapperFileUrl?: string;
    reducerText?: any;
    reducerFileUrl?: string;
    dateCreated?: Moment;
    lastSuccessfulRun?: Moment;
    laboratoryName?: string;
    laboratoryId?: string;
}

export class Algorithm implements IAlgorithm {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public mapperText?: any,
        public mapperFileUrl?: string,
        public reducerText?: any,
        public reducerFileUrl?: string,
        public dateCreated?: Moment,
        public lastSuccessfulRun?: Moment,
        public laboratoryName?: string,
        public laboratoryId?: string
    ) {}
}
