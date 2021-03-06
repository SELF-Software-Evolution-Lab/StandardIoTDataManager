import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IReportParameters } from './report-parameters.model';

export const enum TaskType {
    UNDEFINED = 'UNDEFINED',
    REPORT = 'REPORT',
    FILELOAD = 'FILE_LOAD'
}

export const enum TaskState {
    PENDING = 'PENDING',
    SUBMITTED = 'SUBMITTED',
    PROCESSING = 'PROCESSING',
    COMPLETED = 'COMPLETED',
    ERROR = 'ERROR',
    ALL = 'ALL'
}

export interface IBatchTask {
    id?: string;
    description?: string;
    type?: TaskType;
    state?: TaskState;
    createDate?: Moment;
    startDate?: Moment;
    endDate?: Moment;
    progress?: number;
    user?: string;
    parameters?: IReportParameters;
}

export class BatchTask implements IBatchTask {
    constructor(
        public id?: string,
        public description?: string,
        public type?: TaskType,
        public state?: TaskState,
        public createDate?: Moment,
        public startDate?: Moment,
        public endDate?: Moment,
        public progress?: number,
        public user?: string,
        public parameters?: IReportParameters
    ) {}
}
