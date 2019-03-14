export interface IOperativeRange {
    varName?: string;
    unit?: string;
    minVal?: number;
    maxVal?: number;
}

export class OperativeRange implements IOperativeRange {
    constructor(public varName?: string, public unit?: string, public minVal?: number, public maxVal?: number) {}
}
