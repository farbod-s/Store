<?php

# GET /orders/shippings
$routes[ $this->base . '/shippings' ] = array(
	array( array( $this, 'get_order_shippings' ), WC_API_Server::READABLE ),
);

public function get_order_shippings() {
	$shipping_methods = WC()->shipping() ? WC()->shipping->load_shipping_methods() : array();
	return array( 'shipping_methods' => $shipping_methods );
}

///////////////////////////////////

# GET /orders/payments
$routes[ $this->base . '/payments' ] = array(
	array( array( $this, 'get_order_payments' ), WC_API_Server::READABLE ),
);

public function get_order_payments() {
	$available_gateways = WC()->payment_gateways ? WC()->payment_gateways->get_available_payment_gateways() : array();
	return array( 'payment_methods' => $shipping_methods );
}


//////////////////////////////////////

private function get_active_shipping_methods() {
	$active_methods = array();
	$shipping_methods = WC()->shipping->get_shipping_methods();
	foreach ( $shipping_methods as $id => $shipping_method ) {
		if ( isset( $shipping_method->enabled ) && $shipping_method->enabled == 'yes' ) {
			$active_methods[ $id ] = array( 'title' => $shipping_method->title, 'tax_status' => $shipping_method->tax_status );
		}
	}
	return $active_methods;
}

private function get_active_payment_gateways() {
	$active_gateways = array();
	$gateways = WC()->payment_gateways->payment_gateways();
	foreach ( $gateways as $id => $gateway ) {
		if ( isset( $gateway->enabled ) && $gateway->enabled == 'yes' ) {
			$active_gateways[ $id ] = array( 'title' => $gateway->title, 'supports' => $gateway->supports );
		}
	}
	return $active_gateways;
}

//////////////////////////////////////

# POST /customers/authenticate
$routes[ $this->base . '/authenticate' ] = array(
	array( array( $this, 'authenticate_customer' ),   WC_API_SERVER::CREATABLE | WC_API_Server::ACCEPT_DATA ),
);

public function authenticate_customer( $data ) {
	// Checks the username.
	if ( ! isset( $data['username'] ) ) {
		return new WP_Error( 'woocommerce_api_missing_customer_username', sprintf( __( 'Missing parameter %s', 'woocommerce' ), 'username' ), array( 'status' => 400 ) );
	}

	// Checks the password.
	if ( ! isset( $data['password'] ) ) {
		return new WP_Error( 'woocommerce_api_missing_customer_password', sprintf( __( 'Missing parameter %s', 'woocommerce' ), 'password' ), array( 'status' => 400 ) );
	}

	$user = wp_authenticate($data['username'], $data['password']);

	// Checks for an error in the customer authentication.
	if ( is_wp_error( $user) ) {
		return new WP_Error( 'woocommerce_api_cannot_authenticate_customer', $user->get_error_message(), array( 'status' => 401 ) );
	}
	
	return $this->get_customer( $user->ID );
}

/////////////////////////////////////////////////

// Get the current session data and saved cart
	    $wc_session_data = get_option( '_wc_session_'.$id );

	    // Get the persistent cart
	    $full_user_meta = get_user_meta( $id, '_woocommerce_persistent_cart', true );
	    $cart = $full_user_meta['cart'];

	    if ( is_array( $cart ) ) {
			foreach ( $cart as $key => $values ) {
				$_product = wc_get_product( $values['variation_id'] ? $values['variation_id'] : $values['product_id'] );

				if ( ! empty( $_product ) && $_product->exists() && $values['quantity'] > 0 ) {

					if ( ! $_product->is_purchasable() ) {

						// Flag to indicate the stored cart should be update
						$update_cart_session = true;
						wc_add_notice( sprintf( __( '%s has been removed from your cart because it can no longer be purchased. Please contact us if you need assistance.', 'woocommerce' ), $_product->get_title() ), 'error' );
						do_action( 'woocommerce_remove_cart_item_from_session', $key, $values );

					} else {

						// Put session data into array. Run through filter so other plugins can load their own session data
						$session_data = array_merge( $values, array( 'data' => $_product ) );
						$this->cart->cart_contents[ $key ] = apply_filters( 'woocommerce_get_cart_item_from_session', $session_data, $values, $key );

					}
				}
			}
		}

		WC()->cart = $this->cart;
		WC()->cart->calculate_totals();

		//print_r( WC()->cart->get_cart() );

		// Calculate shipping method rates
		$packages = $this->get_shipping_packages( $id );

	    foreach ( $packages as $package ) {
			$package['rates'] = array();

			foreach ( $this->get_active_shipping_methods() as $shipping_method ) {

				//print_r($shipping_method);

				//if ( $shipping_method->is_available( $package ) ) {

					// Reset Rates
					$shipping_method->rates = array();

					// Calculate Shipping for package
					$shipping_method->calculate_shipping( $package );

					// Place rates in package array
					if ( ! empty( $shipping_method->rates ) && is_array( $shipping_method->rates ) )
						foreach ( $shipping_method->rates as $rate )
							$package['rates'][ $rate->id ] = $rate;
				//}
			}

			print_r( $package['rates'] );

			// Filter the calculated rates
			$package['rates'] = apply_filters( 'woocommerce_package_rates', $package['rates'], $package );
	    }

		// Get chosen methods for each package
	    foreach ($packages as $package) {
	    	$chosen_method    = false;
			$method_count     = false;

			if ( ! empty( $chosen_methods[ $i ] ) ) {
				$chosen_method = $chosen_methods[ $i ];
			}

			if ( ! empty( $method_counts[ $i ] ) ) {
				$method_count = $method_counts[ $i ];
			}

			// Get available methods for package
			$available_methods = $package['rates'];

			print_r($available_methods);

			if ( sizeof( $available_methods ) > 0 ) {

				// If not set, not available, or available methods have changed, set to the DEFAULT option
				if ( empty( $chosen_method ) || ! isset( $available_methods[ $chosen_method ] ) || $method_count != sizeof( $available_methods ) ) {
					$chosen_method        = apply_filters( 'woocommerce_shipping_chosen_method', WC()->shipping->get_default_method( $available_methods, $chosen_method ), $available_methods );
					$chosen_methods[ $i ] = $chosen_method;
					$method_counts[ $i ]  = sizeof( $available_methods );
					do_action( 'woocommerce_shipping_method_chosen', $chosen_method );
				}

				// Store total costs
				if ( $chosen_method ) {
					$rate = $available_methods[ $chosen_method ];

					// Merge cost and taxes - label and ID will be the same
					WC()->shipping->shipping_total += $rate->cost;

					if ( ! empty( $rate->taxes ) && is_array( $rate->taxes ) ) {
						foreach ( array_keys( WC()->shipping->shipping_taxes + $rate->taxes ) as $key ) {
							WC()->shipping->shipping_taxes[ $key ] = ( isset( $rate->taxes[$key] ) ? $rate->taxes[$key] : 0 ) + ( isset( WC()->shipping->shipping_taxes[$key] ) ? WC()->shipping->shipping_taxes[$key] : 0 );
						}
					}
				}
			}
	    }

	    //print_r( WC()->cart );

	    // Get totals for the chosen shipping method
		print_r( 'shipping_total='.WC()->shipping->shipping_total );	// Shipping Total
		print_r( 'shipping_taxes='.WC()->shipping->shipping_taxes );	// Shipping Taxes

?>

"e369853df766fa44e1ed0ff613f563bd": {
	"product_id": 34,
	"variation_id": 0,
	"variation": [],
	"quantity": 1,
	"line_total": 20000,
	"line_tax": 0,
	"line_subtotal": 20000,
	"line_subtotal_tax": 0,
	"line_tax_data": {
		"total": [],
		"subtotal": []
	}
}