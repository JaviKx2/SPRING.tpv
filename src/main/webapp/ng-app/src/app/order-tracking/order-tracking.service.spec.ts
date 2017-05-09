import { TestBed, async, inject } from '@angular/core/testing';
import { HttpModule, Response, ResponseOptions, BaseRequestOptions, Http } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';


import { ProductState }  from './product-state';
import { OrderTrackingService } from './order-tracking.service';
import { TPVService } from '../shared/tpv.service';


let productStateMock = [];
productStateMock.push(new ProductState("article0", "article0", "OPENED"));

describe('Service: OrderTrackingService', () => {

  let product_code:string = 'article0';
  let orderTrackingService:OrderTrackingService;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpModule],
      providers: [ 
      OrderTrackingService,
      MockBackend,
      BaseRequestOptions,
      TPVService,
      {
        provide: Http,
        useFactory: (backend, options) => new Http(backend, options),
        deps: [MockBackend, BaseRequestOptions]
      }
      ]
    });
    orderTrackingService = TestBed.get(OrderTrackingService);
  }));

  it(`Should get the ticket state whem 'getTicket()' is called`, inject([MockBackend], (mockBackend:MockBackend) => {
    mockBackend.connections.subscribe(conn => {
      conn.mockRespond(new Response(new ResponseOptions({ body: JSON.stringify(productStateMock) })));
    });
    orderTrackingService.getTicket('testTicket').then(productState=>{
         let product:ProductState = productState[0];
         expect(product.shoppingState).toBe('OPENED');
    })
  }));

});