export interface IDevice {
    internalId?: string;
    name?: string;
    description?: string;
}

export class Device implements IDevice {
    constructor(public internalId?: string, public name?: string, public description?: string) {}
}
