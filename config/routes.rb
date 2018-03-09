Rails.application.routes.draw do
  resources :mentors do
    collection { post :import }
  end
  devise_for :users
  resources :orders do
    collection { get :scan }
    collection { post :scan_student }
  end
  resources :students do
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

  post 'barcode_order' => 'orders#create_barcode_order'
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
