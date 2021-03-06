import { Moment } from 'moment';

export interface IExperiment {
    id?: string;
    name?: string;
    description?: string;
    notes?: any;
    created?: Moment;
    createdBy?: string;
    systemName?: string;
    systemId?: string;
    tags?: string[];
}

export class Experiment implements IExperiment {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public notes?: any,
        public created?: Moment,
        public createdBy?: string,
        public systemName?: string,
        public systemId?: string,
        public tags?: string[]
    ) {}
}
