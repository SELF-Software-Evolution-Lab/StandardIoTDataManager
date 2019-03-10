export interface ISensor {
    id?: string;
    internalId?: string;
    sensorType?: string;
    potentialFreq?: number;
    samplingFreq?: number;
}

export class Sensor implements ISensor {
    constructor(
        public id?: string,
        public internalId?: string,
        public sensorType?: string,
        public potentialFreq?: number,
        public samplingFreq?: number
    ) {}
}
