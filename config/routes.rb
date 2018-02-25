Rails.application.routes.draw do
  devise_for :users
  resources :orders
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

  get 'new_qr' => 'orders#new_qr'
  post 'qr_order' => 'orders#create_qr_order'
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
