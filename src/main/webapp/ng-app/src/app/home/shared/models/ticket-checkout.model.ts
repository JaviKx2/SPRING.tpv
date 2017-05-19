/**
  * @author Sergio Banegas Cortijo
  * Github: https://github.com/sergiobanegas 
*/
import { CartProduct } from './cart-product.model';

export class TicketCheckout {
	constructor(public shoppingList: CartProduct[], public userMobile?: number) {
		this.shoppingList = shoppingList;
	}
}