<?php
/**
 * WooCommerce API Carts Class
 *
 * Handles requests to the /carts endpoint
 *
 * @author      Farbod Samsamipour
 * @category    API
 * @package     WooCommerce/API
 */

if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly
}

class WC_API_Carts extends WC_API_Resource {

	protected $base = '/carts';

	private $cart = null;
	private $packages = array();

	public function __construct( WC_API_Server $server ) {
		parent::__construct( $server );
	}

	public function register_routes( $routes ) {

		# GET/POST/PUT/DELETE /carts/<id>
		$routes[ $this->base . '/(?P<id>\d+)' ] = array(
			array( array( $this, 'get_cart' ),    WC_API_Server::READABLE ),
			array( array( $this, 'create_cart' ), WC_API_Server::CREATABLE | WC_API_Server::ACCEPT_DATA ),
			array( array( $this, 'update_cart' ), WC_API_SERVER::EDITABLE | WC_API_SERVER::ACCEPT_DATA ),
			array( array( $this, 'delete_cart' ), WC_API_SERVER::DELETABLE ),
		);

		# GET /carts/<id>/count
		$routes[ $this->base . '/(?P<id>\d+)/count'] = array(
			array( array( $this, 'get_cart_count' ), WC_API_Server::READABLE ),
		);

		# GET /carts/<id>/items
		$routes[ $this->base . '/(?P<id>\d+)/items' ] = array(
			array( array( $this, 'get_cart_items' ), WC_API_Server::READABLE ),
		);

		# POST/PUT/DELETE /carts/<id>/item
		$routes[ $this->base . '/(?P<id>\d+)/item' ] = array(
			array( array( $this, 'create_cart_item' ), WC_API_Server::CREATABLE | WC_API_Server::ACCEPT_DATA ),
			array( array( $this, 'update_cart_item' ), WC_API_SERVER::EDITABLE | WC_API_SERVER::ACCEPT_DATA ),
			array( array( $this, 'delete_cart_item' ), WC_API_SERVER::DELETABLE ),
		);

		return $routes;
	}

	public function get_cart( $id ) {
		$cart_loaded = $this->load_wc_cart( $id );
		if ( $cart_loaded ) {
			print_r( WC()->cart->cart_contents );
		}
	}

	public function create_cart( $id, $data ) {
		// TODO
	}

	public function update_cart( $id, $data ) {
		// TODO
	}

	public function delete_cart( $id ) {
		// TODO
	}

	public function get_cart_count( $id ) {
		// TODO
	}

	public function get_cart_items( $id ) {
		// TODO
	}

	public function create_cart_item( $id, $data ) {
		// TODO
	}

	public function update_cart_item( $id, $data ) {
		// TODO
	}

	public function delete_cart_item( $id, $data ) {
		// TODO
	}




	private function load_wc_cart( $user_id ) {
		$saved_cart = get_user_meta( $user_id, '_woocommerce_persistent_cart', true );
		$cart = $saved_cart['cart'];
		
		if ( is_array( $cart ) ) {
			foreach ( $cart as $key => $values ) {
				$_product = wc_get_product( $values['variation_id'] ? $values['variation_id'] : $values['product_id'] );
				
				if ( ! empty( $_product ) && $_product->exists() && $values['quantity'] > 0 ) {
					if ( $_product->is_purchasable() ) {
						$session_data = array_merge( $values, array( 'data' => $_product ) );
						WC()->cart->cart_contents[ $key ] = $session_data;
					}
				}
			}
			
			return true;
		}
		
		return false;
	}

	private function save_wc_cart( $user_id ) {
		$saved_cart = get_user_meta( $user_id, '_woocommerce_persistent_cart', true );
		$saved_cart['cart'] = WC()->cart->cart_contents;
		update_user_meta( $user_id, '_woocommerce_persistent_cart', $saved_cart );
	}

	private function get_active_shipping_methods() {
		$active_methods = array();
		$shipping_methods = WC()->shipping() ? WC()->shipping->load_shipping_methods() : array();
		
		foreach ( $shipping_methods as $shipping_method ) {
			if ( isset( $shipping_method->enabled ) && $shipping_method->enabled == 'yes' ) {
				$active_methods[] = $shipping_method;
			}
		}

		return $active_methods;
	}

	private function get_active_payment_gateways() {
		$active_gateways = array();
		$gateways = WC()->payment_gateways->payment_gateways();
		
		foreach ( $gateways as $id => $gateway ) {
			if ( isset( $gateway->enabled ) && $gateway->enabled == 'yes' ) {
				$active_gateways[] = $gateway;
			}
		}

		return $active_gateways;
	}

	private function get_shipping_packages( $user_id ) {
		$packages = array();
		$customer = new WP_User( $user_id );

		$packages[0]['contents']                 = WC()->cart->get_cart();		// Items in the package
		$packages[0]['contents_cost']            = 0;							// Cost of items in the package, set below
		$packages[0]['applied_coupons']          = WC()->cart->applied_coupons;
		$packages[0]['user']['ID']               = $user_id;
		$packages[0]['destination']['country']   = $customer->shipping_country;
		$packages[0]['destination']['state']     = $customer->shipping_state;
		$packages[0]['destination']['postcode']  = $customer->shipping_postcode;
		$packages[0]['destination']['city']      = $customer->shipping_city;
		$packages[0]['destination']['address']   = $customer->shipping_address_1;
		$packages[0]['destination']['address_2'] = $customer->shipping_address_2;

		foreach ( WC()->cart->get_cart() as $item ) {
			if ( $item['data']->needs_shipping() ) {
				if ( isset( $item['line_total'] ) ) {
					$packages[0]['contents_cost'] += $item['line_total'];
				}
			}
		}

		return $packages;
	}
}