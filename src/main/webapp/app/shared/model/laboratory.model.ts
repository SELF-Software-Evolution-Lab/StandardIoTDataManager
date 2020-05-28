import { Moment } from 'moment';
import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { ISubSet } from 'app/shared/model/sub-set.model';

export interface ILaboratory {
    id?: string;
    name?: string;
    description?: string;
    dateCreated?: Moment;
    shareUrl?: string;
    shareValidThru?: Moment;
    tags?: string;
    algorithms?: IAlgorithm[];
    subSets?: ISubSet[];
    samplingName?: string;
    samplingId?: string;
}

export class Laboratory implements ILaboratory {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public dateCreated?: Moment,
        public shareUrl?: string,
        public shareValidThru?: Moment,
        public tags?: string,
        public algorithms?: IAlgorithm[],
        public subSets?: ISubSet[],
        public samplingName?: string,
        public samplingId?: string
    ) {}
}
