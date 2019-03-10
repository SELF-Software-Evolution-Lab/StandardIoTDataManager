export interface ITargetSystem {
    id?: string;
    name?: string;
    description?: string;
    organizationName?: string;
    organizationId?: string;
}

export class TargetSystem implements ITargetSystem {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public organizationName?: string,
        public organizationId?: string
    ) {}
}
