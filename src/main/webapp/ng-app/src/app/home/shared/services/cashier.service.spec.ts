import { TestBed, async, inject } from '@angular/core/testing';
import { HttpModule, Response, ResponseOptions, BaseRequestOptions, Http } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';

import { CartProduct } from '../models/cart-product';
import { CashierService } from './cashier.service';
import { CashierClosure } from '../models/cashier-closure';

import { HTTPService } from '../../../shared/services/http.service';

export const MockCashierClosureOpen = {
  id: 1,
  openingDate: new Date(),
  closingDate: null,
  comment: null
}

export const MockCashierClosureClose = {
  id: 1,
  openingDate: new Date(),
  closingDate: new Date(),
  comment: null
}

describe('Service: CashierService', () => {

  let cashierService: CashierService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ HttpModule ],
      providers: [ 
        CashierService,
        MockBackend,
        BaseRequestOptions,
        HTTPService,
        {
          provide: Http,
          useFactory: (backend, options) => new Http(backend, options),
          deps: [ MockBackend, BaseRequestOptions ]
        }
      ]
    });
    cashierService = TestBed.get(CashierService);
    cashierService.initialize();
  }));

  it(`Should open the cashier when 'openCashier()' is called`, inject([MockBackend], (mockBackend: MockBackend) => {  
      mockBackend.connections.subscribe((conn: MockConnection) => {
        conn.mockRespond(new Response(new ResponseOptions({ body: JSON.stringify(MockCashierClosureOpen) })));
      });
      cashierService.openCashier().then((cashier: CashierClosure)=>{
        expect(cashierService.getCurrentCashier().closureDate).toBe(null);
      });
   }));

  it(`Should make a withdrawal  when 'withdraw()' is called`, inject([MockBackend], (mockBackend: MockBackend) => {  
      mockBackend.connections.subscribe((conn: MockConnection) => {
        conn.mockRespond(new Response(new ResponseOptions({ body: JSON.stringify(MockCashierClosureOpen) })));
      });
      cashierService.withdraw(10).then((cashier: CashierClosure)=>{
        expect(cashierService.getCurrentCashier().amount).toBe(42);
      });
  }));

  it(`Should make a deposit 'deposit()' is called`, inject([MockBackend], (mockBackend: MockBackend) => {
    mockBackend.connections.subscribe((conn: MockConnection) => {
        conn.mockRespond(new Response(new ResponseOptions({ body: JSON.stringify(MockCashierClosureOpen) })));
    });  
    cashierService.deposit(5).then((cashier: CashierClosure)=>{
        expect(cashierService.getCurrentCashier().amount).toBe(57);
      });
  }));

  it(`Should close the cashier when 'closeCashier()' is called`, inject([MockBackend], (mockBackend: MockBackend) => {  
      mockBackend.connections.subscribe((conn: MockConnection) => {
        conn.mockRespond(new Response(new ResponseOptions({ body: JSON.stringify(MockCashierClosureClose) })));
      });
      cashierService.closeCashier(123, 'comment').then((cashier: CashierClosure)=>{
        expect(cashierService.getCurrentCashier().closureDate).not.toBe(null);
        expect(cashierService.getCurrentCashier().amount).toBe(123);
      });
  }));


});