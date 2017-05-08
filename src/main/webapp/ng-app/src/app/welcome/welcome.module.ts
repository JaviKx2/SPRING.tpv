/**
 * Created by fran lopez on 07/05/2017.
 */

import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {WelcomeComponent} from './welcome.component';
import {LoginComponent} from './login/login.component';
import {TicketComponent} from './ticket/ticket.component';
import {WelcomeRoutingModule} from './welcome.routes';
import {RequestService} from '../shared/request.service';
import {LoginService} from './login/login.service';
import {TokenService} from './login/token.service';
import {AngularMaterialModule} from '../shared/angular-material.module';

@NgModule({
    declarations: [
        WelcomeComponent,
        LoginComponent,
        TicketComponent
    ],
    imports: [
        CommonModule,
        WelcomeRoutingModule,
        FormsModule,
        HttpModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        AngularMaterialModule
    ],
    providers: [
        RequestService,
        LoginService,
        TokenService
    ],
})
export class WelcomeModule {
}