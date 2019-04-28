import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export const enum TypeTask {
    UNDEFINED = 'UNDEFINED',
    REPORT = 'REPORT',
    FILELOAD = 'FILE_LOAD'
}

export const enum StateTask {
    PENDING = 'PENDING',
    PROCESSING = 'PROCESSING',
    COMPLETED = 'COMPLETED',
    ERROR = 'ERROR'
}

export interface IBatchTask {
    id?: string;
    description?: string;
    type?: TypeTask;
    state?: StateTask;
    createDate?: Moment;
    startDate?: Moment;
    endDate?: Moment;
    progress?: number;
    user?: IUser;
}

export class BatchTask implements IBatchTask {
    constructor(
        public id?: string,
        public description?: string,
        public type?: TypeTask,
        public state?: StateTask,
        public createDate?: Moment,
        public startDate?: Moment,
        public endDate?: Moment,
        public progress?: number,
        public user?: IUser
    ) {}
}
