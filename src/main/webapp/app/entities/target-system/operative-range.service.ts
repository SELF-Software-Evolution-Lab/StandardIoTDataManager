import { Injectable } from '@angular/core';
import { IOperativeRange } from 'app/shared/model/operative-range.model';

@Injectable({ providedIn: 'root' })
export class OperativeRangeService {
    private _toModify: IOperativeRange;
    private _toModifyIndex: number;
    private _newValue: IOperativeRange;

    reset() {
        this.toModify = undefined;
        this.toModifyIndex = undefined;
        this.newValue = undefined;
    }

    get toModify(): IOperativeRange {
        return this._toModify;
    }

    set toModify(value: IOperativeRange) {
        this._toModify = value;
    }

    get toModifyIndex(): number {
        return this._toModifyIndex;
    }

    set toModifyIndex(value: number) {
        this._toModifyIndex = value;
    }

    get newValue(): IOperativeRange {
        return this._newValue;
    }

    set newValue(value: IOperativeRange) {
        this._newValue = value;
    }
}
