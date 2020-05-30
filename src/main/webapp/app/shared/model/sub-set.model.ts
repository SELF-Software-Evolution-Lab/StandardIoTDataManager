import { Moment } from 'moment';

export interface ISubSet {
    id?: string;
    name?: string;
    description?: string;
    fileHdfsLocation?: string;
    dateCreated?: Moment;
    downloadUrl?: string;
    laboratoryName?: string;
    laboratoryId?: string;
}

export class SubSet implements ISubSet {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public fileHdfsLocation?: string,
        public dateCreated?: Moment,
        public downloadUrl?: string,
        public laboratoryName?: string,
        public laboratoryId?: string
    ) {}
}
