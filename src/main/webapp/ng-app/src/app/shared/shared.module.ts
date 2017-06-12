/**
 * @author Fran López
 * @author Sergio Banegas Cortijo
 * Github: https://github.com/sergiobanegas
 */

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DateComponent} from './directives/date.component';
import {CapitalizePipe} from './pipes/capitalize.pipe';
import {BooleanToStringPipe} from './pipes/bool-to-str.pipe';

@NgModule({
    imports: [CommonModule],
    declarations: [DateComponent, CapitalizePipe, BooleanToStringPipe],
    exports: [DateComponent, CapitalizePipe, BooleanToStringPipe]
})
export class SharedModule {
}