export interface ISensor {
    internalId?: string;
    sensorType?: string;
    samplingUnit?: string;
    potentialFreq?: number;
    samplingFreq?: number;
    deviceId?: string;
    deviceName?: string;
}

export class Sensor implements ISensor {
    constructor(
        public internalId?: string,
        public sensorType?: string,
        public potentialFreq?: number,
        public samplingFreq?: number,
        public deviceId?: string,
        public deviceName?: string,
        public samplingUnit?: string
    ) {}
}

export class SelectableSensor {
    label: string;

    constructor(public internalId?: string, public sensorType?: string) {
        this.label = `${internalId} - ${sensorType}`;
    }
}
