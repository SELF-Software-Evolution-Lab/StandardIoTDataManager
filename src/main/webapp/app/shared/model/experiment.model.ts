export interface IExperiment {
    id?: string;
    name?: string;
    description?: string;
    notes?: string;
    systemName?: string;
    systemId?: string;
}

export class Experiment implements IExperiment {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public notes?: string,
        public systemName?: string,
        public systemId?: string
    ) {}
}
