class ApplicationController < ActionController::Base
  add_flash_types :success, :info, :warning, :error
  protect_from_forgery with: :exception
  before_action :authenticate_user!
  after_filter :flash_to_headers

  def flash_to_headers
      return unless request.xhr?
      response.headers['X-Message'] = flash_message
      response.headers["X-Message-Type"] = flash_type.to_s

      flash.discard # don't want the flash to appear when you reload page
  end

  private

  def flash_message
      [:error, :warning, :notice].each do |type|
          return flash[type].blank? ? nil : flash[type]
      end
  end

  def flash_type
      [:error, :warning, :notice].each do |type|
          return flash[type].blank? ? nil : type
      end
  end
end
