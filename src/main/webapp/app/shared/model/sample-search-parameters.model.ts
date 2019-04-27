import { Moment } from 'moment';

export interface ISampleSearchParameters {
    targetSystemId?: string[];
    sensorsId?: string[];
    fromDateTime?: Moment;
    toDateTime?: Moment;
    tags?: string[];
}

export class SampleSearchParameters implements ISampleSearchParameters {
    constructor(
        public targetSystemId?: string[],
        public sensorsId?: string[],
        public fromDateTime?: Moment,
        public toDateTime?: Moment,
        public tags?: string[]
    ) {}
}
