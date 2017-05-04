import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { ToastyModule } from 'ng2-toasty';

import { AppComponent } from './app.component';

import { WelcomeModule } from './welcome/welcome.module';
import { HomeModule } from './home/home.module';
import { AppRoutingModule } from './app-routing.module';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    WelcomeModule,
    HomeModule,
    AppRoutingModule,
    BrowserModule,
    ToastyModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }