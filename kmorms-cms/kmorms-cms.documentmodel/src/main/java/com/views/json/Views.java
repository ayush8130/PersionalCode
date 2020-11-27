package com.views.json;

public interface Views {

	/**
	 * Essential only fields that can be exposed via the public interface
	 */
	public interface PublicLight {
	}

	/**
	 * Fields that can be exposed via the public interface.
	 */
	public interface Public extends PublicLight {
	}

	/**
	 * Fields that can be exposed via the DATA (CMS) interface.
	 */
	public interface Data extends Public {
	}

	/**
	 * Fields that can be exposed as part of the Catalogue.
	 */
	public interface PublicCatalogue {
	}

	/**
	 * Essential only fields to be exposed as part of reservation details
	 */
	public interface ReservationDetails {
	}

	/**
	 * Fields that can be exposed as via Seller interface.
	 */
	public interface Seller extends PublicLight {
	}


	
}
