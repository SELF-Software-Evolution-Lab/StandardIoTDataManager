import { Moment } from 'moment';

export interface ISample {
    id?: string;
    dateTime?: Moment;
    ts?: Moment;
    sensorInternalId?: string;
    samplingId?: string;
    experimentId?: string;
    targetSystemId?: string;
    measurements: string[];
}

export class Sample implements ISample {
    constructor(
        public id?: string,
        public dateTime?: Moment,
        public ts?: Moment,
        public sensorInternalId?: string,
        public samplingId?: string,
        public experimentId?: string,
        public targetSystemId?: string,
        public measurements = []
    ) {}
}
