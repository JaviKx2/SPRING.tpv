/**
  * @author Sergio Banegas Cortijo
  * Github: https://github.com/sergiobanegas
*/
import { TestBed, async, inject } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { MaterialModule } from '@angular/material';
import { FlexLayoutModule } from '@angular/flex-layout';
import 'hammerjs';

import { SharedModule } from '../shared/shared.module';

import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { HomeComponent } from './home.component';

import { AuthService } from './shared/services/auth.service';
import { ToastService } from '../shared/services/toast.service';
import { LocalStorageService } from '../shared/services/local-storage.service';
import { HTTPService } from '../shared/services/http.service';
import { ToastyService, ToastyConfig, ToastOptions, ToastData } from 'ng2-toasty';


describe('Component: HomeComponent', () => {

  let home: HomeComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ MaterialModule, FlexLayoutModule, FormsModule, BrowserAnimationsModule, SharedModule ],
      declarations: [ HomeComponent, ShoppingCartComponent ],
      providers: [
        { provide: Router },
        AuthService,
        ToastService,
        LocalStorageService,
        HTTPService,
        ToastyService, ToastyConfig, ToastOptions, ToastData
      ]
    });
    let fixture: any = TestBed.createComponent(HomeComponent);
    home = fixture.componentInstance;
  }));

});
