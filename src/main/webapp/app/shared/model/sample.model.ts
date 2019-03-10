import { Moment } from 'moment';

export interface ISample {
    id?: string;
    ts?: Moment;
    sensorInternalId?: string;
    samplingId?: string;
}

export class Sample implements ISample {
    constructor(public id?: string, public ts?: Moment, public sensorInternalId?: string, public samplingId?: string) {}
}
