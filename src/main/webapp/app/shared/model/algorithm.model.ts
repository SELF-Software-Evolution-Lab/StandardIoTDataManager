import { Moment } from 'moment';
import { ISubSet } from 'app/shared/model/sub-set.model';

export const enum SubSetType {
    TRAINING = 'TRAINING',
    VALIDATION = 'VALIDATION'
}

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
    setType?: SubSetType;
    laboratoryName?: string;
    laboratoryId?: string;
    subSets?: ISubSet[];
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
        public setType?: SubSetType,
        public laboratoryName?: string,
        public laboratoryId?: string,
        public subSets?: ISubSet[]
    ) {}
}
