import { Moment } from 'moment';
import { IDevice } from 'app/shared/model/device.model';
import { IOperativeCondition } from 'app/shared/model/operative-condition.model';
import { ISensor } from 'app/shared/model/sensor.model';

export interface ISampling {
    id?: string;
    name?: string;
    notes?: any;
    startTime?: Moment;
    endTime?: Moment;
    experimentName?: string;
    experimentId?: string;
    devices?: IDevice[];
    sensors?: ISensor[];
    tags?: string[];
    operativeConditions?: IOperativeCondition[];
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
        public tags?: string[],
        public operativeConditions?: IOperativeCondition[],
        public sensors?: ISensor[]
    ) {}
}
