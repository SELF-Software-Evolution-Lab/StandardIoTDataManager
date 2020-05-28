export const enum TagType {
    ANALYSIS_PURPOSE = 'ANALYSIS_PURPOSE',
    FAILURE_MODE = 'FAILURE_MODE'
}

export interface ITag {
    id?: string;
    name?: string;
    type?: TagType;
}

export class Tag implements ITag {
    constructor(public id?: string, public name?: string, public type?: TagType) {}
}
