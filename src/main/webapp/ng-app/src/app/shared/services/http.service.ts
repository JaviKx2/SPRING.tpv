/**
 * @author Fran lopez
 *
 * @author Sergio Banegas Cortijo
 * Github: https://github.com/sergiobanegas 
*/
import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions, URLSearchParams, ResponseContentType} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class HTTPService {

	constructor (private http:Http){}

	get(endpoint:string, headers?:Headers, params?: URLSearchParams): Observable<any> {
		let options = new RequestOptions({headers: headers, params: params});
		return this.http.get(endpoint, options).map(this.extractData).catch(this.handleError);
	}

	post(endpoint:string, body?:Object, headers?:Headers, params?: URLSearchParams): Observable<any> {
		let options = new RequestOptions({headers: headers});
		if (headers && headers.has('accept') && headers.get('accept') === 'application/pdf'){
			options.responseType = ResponseContentType.Blob;
		}
		return this.http.post(endpoint, body, options).map(this.extractData).catch(this.handleError);
	}

	put(endpoint:string, body?:Object, headers?:Headers, params?: URLSearchParams): Observable<any> {
		let options = new RequestOptions({headers: headers, params: params});
		return this.http.put(endpoint, body, options).map(this.extractData).catch(this.handleError);
	}

	delete(endpoint:string, headers?: Headers, params?: URLSearchParams): Observable<any> {
		let options = new RequestOptions({headers: headers, params: params});
		return this.http.delete(endpoint, options).map(this.extractData).catch(this.handleError);
	}

	private extractData(res: Response): any {
		if (res.text()){
			if (res.headers.get('content-type').indexOf('application/pdf') !== -1){
				return res.blob();
			} else if (res.headers.get('content-type').indexOf('application/json') !== -1){
				return res.json();
			}
		} else {
			return res;
		}
	}
	private handleError (error: Response | any): any {
		return Observable.throw(error.message || error.json());
	}
}