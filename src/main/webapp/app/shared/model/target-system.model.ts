import { Moment } from 'moment';
import { IOperativeRange } from 'app/shared/model/operative-range.model';

export interface ITargetSystem {
    id?: string;
    name?: string;
    description?: string;
    created?: Moment;
    createdBy?: string;
    organizationName?: string;
    organizationId?: string;
    operativeRanges: IOperativeRange[];
}

export class TargetSystem implements ITargetSystem {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public createdBy?: string,
        public organizationName?: string,
        public operativeRanges: IOperativeRange[] = []
    ) {}
}
