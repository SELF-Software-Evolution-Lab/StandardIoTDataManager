import { Moment } from 'moment';

export interface IOrganization {
    id?: string;
    name?: string;
    description?: string;
    created?: Moment;
    createdBy?: string;
}

export class Organization implements IOrganization {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public createdBy?: string
    ) {}
}
