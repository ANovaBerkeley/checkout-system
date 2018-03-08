Rails.application.routes.draw do
  devise_for :users
  resources :orders do
    collection { get :scan }
    collection { post :scan_member }
  end
  resources :members do
    collection { post :import }
    collection { post :delete_all }
  end
  resources :users
  resources :items do
    collection { post :import }
  end

  root 'orders#index'
  get 'renew/:id' => 'orders#renew'
  get 'return/:id' => 'orders#disable'
  get 'past_orders' => 'orders#old'

<<<<<<< HEAD
  post 'barcode_order' => 'orders#create_barcode_order'
=======
  get 'new_qr' => 'orders#new_qr'
  post 'qr_order' => 'orders#create_qr_order'
>>>>>>> 8169734b02debbe7c77c2779aab28d61ccc897a8
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
