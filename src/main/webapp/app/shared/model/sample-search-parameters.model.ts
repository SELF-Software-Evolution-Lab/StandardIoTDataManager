import { Moment } from 'moment';
import { OperativeRange } from 'app/shared/model/operative-range.model';

export interface ISampleSearchParameters {
    targetSystemId?: string[];
    sensorsId?: string[];
    operativeConditions?: OperativeRange[];
    fromDateTime?: Moment;
    toDateTime?: Moment;
    tags?: string[];
}

export class SampleSearchParameters implements ISampleSearchParameters {
    constructor(
        public targetSystemId?: string[],
        public sensorsId?: string[],
        public operativeConditions?: OperativeRange[],
        public fromDateTime?: Moment,
        public toDateTime?: Moment,
        public tags?: string[]
    ) {}
}
