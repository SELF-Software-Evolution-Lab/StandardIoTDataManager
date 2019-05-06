import { Moment } from 'moment';
export interface IReportParameters {
    expectedRecords?: number;
    tags?: string[];
    targetSystemId?: string[];
    experimentsId?: string[];
    sensorsId?: string[];
    fromDateTime?: Moment;
    toDateTime?: Moment;
    totalLines?: number;
    processedLines?: number;
    processedLinesOk?: number;
}

export class ReportParameters implements IReportParameters {
    constructor(
        public expectedRecords?: number,
        public tags?: string[],
        public targetSystemId?: string[],
        public experimentsId?: string[],
        public sensorsId?: string[],
        public fromDateTime?: Moment,
        public toDateTime?: Moment,
        public totalLines?: number,
        public processedLines?: number,
        public processedLinesOk?: number
    ) {}
}
