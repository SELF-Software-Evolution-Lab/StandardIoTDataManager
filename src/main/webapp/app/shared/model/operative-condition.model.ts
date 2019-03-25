export interface IOperativeCondition {
    varName?: string;
    unit?: string;
    value?: number;
}

export class OperativeCondition implements IOperativeCondition {
    constructor(public varName?: string, public unit?: string, public value?: number) {}
}
