import { ISensor } from 'app/shared/model/sensor.model';

export interface IDevice {
    id?: string;
    internalId?: string;
    name?: string;
    description?: string;
    sensors?: ISensor[];
}

export class Device implements IDevice {
    constructor(
        public id?: string,
        public internalId?: string,
        public name?: string,
        public description?: string,
        public sensors?: ISensor[]
    ) {}
}