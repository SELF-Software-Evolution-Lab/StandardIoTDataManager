import { Moment } from 'moment';
import { IDevice } from 'app/shared/model/device.model';

export interface ISampling {
    id?: string;
    name?: string;
    notes?: any;
    startTime?: Moment;
    endTime?: Moment;
    experimentName?: string;
    experimentId?: string;
    devices?: IDevice[];
    tags?: string[];
}

export class Sampling implements ISampling {
    constructor(
        public id?: string,
        public name?: string,
        public notes?: any,
        public startTime?: Moment,
        public endTime?: Moment,
        public experimentName?: string,
        public experimentId?: string,
        public devices?: IDevice[],
        public tags?: string[]
    ) {}
}
