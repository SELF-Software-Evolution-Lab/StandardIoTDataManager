export interface IOrganization {
    id?: string;
    name?: string;
    description?: string;
}

export class Organization implements IOrganization {
    constructor(public id?: string, public name?: string, public description?: string) {}
}
