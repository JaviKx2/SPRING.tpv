import { TestBed, async, inject } from '@angular/core/testing';
import { HttpModule, Response, ResponseOptions, BaseRequestOptions, Http } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

import { CartProduct } from '../models/cart-product';
import { User } from '../../../shared/models/user';

import { ShoppingCartService } from './shopping-cart.service';
import { LocalStorageService } from '../../../shared/services/local-storage.service';
import { HTTPService } from '../../../shared/services/http.service';

export const MockProduct = {
  id: 0,
  code: 'article6',
  description: 'article6',
  retailPrice: 20.00,
  discontinued: false
}

export const MockTicket = {
  ticketReference: '12341234'
}

describe('Service: ShoppingCartService', () => {

  let product_code: string = 'article6';
  let shoppingCartService: ShoppingCartService;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpModule],
      providers: [ 
      ShoppingCartService,
      LocalStorageService,
      MockBackend,
      BaseRequestOptions,
      HTTPService,
      {
        provide: Http,
        useFactory: (backend, options) => new Http(backend, options),
        deps: [MockBackend, BaseRequestOptions]
      }
      ]
    });
    shoppingCartService = TestBed.get(ShoppingCartService);
  }));

  afterEach(() => {
    shoppingCartService.clear();
  });

  it(`Should add product to cart when 'addProduct()' is called`, inject([MockBackend], (mockBackend: MockBackend) => {
    mockBackend.connections.subscribe((conn: MockConnection) => {
      conn.mockRespond(new Response(new ResponseOptions({ body: JSON.stringify(MockProduct) })));
    });
    shoppingCartService.addProduct(product_code).then(()=>{
      expect(shoppingCartService.getCartProducts()).toContain(new CartProduct('article6', 'article6', 20));
    });
  }));

  it(`Should remove product with code '${product_code}' of cart`, () => {
    shoppingCartService.addProduct(product_code);
    shoppingCartService.removeProduct(new CartProduct(product_code, 'DESCRIPTION1', 12.21));
    let found: boolean = false;
    shoppingCartService.getCartProducts().forEach((cartProduct: CartProduct) => {
      if (cartProduct.productCode === product_code) found = true;
    });
    expect(found).toBe(false);
  });

  it(`Should update the product data when 'updateProduct()' method is called`, () => {
    shoppingCartService.addProduct(product_code).then(() => {
      let cartProduct: CartProduct = new CartProduct(product_code, 'DESCRIPTION1', 12.21);
      cartProduct.delivered = false;
      shoppingCartService.updateProduct(cartProduct);
      expect(shoppingCartService.getCartProducts()[0].delivered).toBe(false);
    });
  });

  it(`Should clear the cart when 'clear()' method is called`,() => {
    shoppingCartService.addProduct(product_code);
    shoppingCartService.clear();
    expect(shoppingCartService.getCartProducts().length).toBe(0);
  });

  it(`Should obtain the total price when 'getTotalPrice()' method is called`, () => {
    shoppingCartService.addProduct(product_code).then(() => {
      let totalPrice:number = shoppingCartService.getTotalPrice();
      expect(totalPrice).toBe(20);
    }); 
  });

  it(`Should associate an user when 'associateUser()' method is called`, () => {
    shoppingCartService.associateUser(666000002).then((userAssociated: User) => {
      expect(userAssociated.mobile).toBe(666000002);
    });
  });

  it(`Should disassociate an user when 'disassociateUser()' method is called`, () => {
    shoppingCartService.disassociateUser();
     expect(shoppingCartService.getUserMobile()).toBeNull();
  });

});
