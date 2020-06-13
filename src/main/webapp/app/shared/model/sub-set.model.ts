import { Moment } from 'moment';

export const enum SubSetType {
    TRAINING = 'TRAINING',
    VALIDATION = 'VALIDATION'
}

export interface ISubSet {
    id?: string;
    name?: string;
    description?: string;
    fileHdfsLocation?: string[];
    dateCreated?: Moment;
    downloadUrl?: string[];
    setType?: SubSetType;
    laboratoryName?: string;
    laboratoryId?: string;
    algorithmId?: string;
}

export class SubSet implements ISubSet {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public fileHdfsLocation?: string[],
        public dateCreated?: Moment,
        public downloadUrl?: string[],
        public setType?: SubSetType,
        public laboratoryName?: string,
        public laboratoryId?: string,
        public algorithmId?: string
    ) {}
}
