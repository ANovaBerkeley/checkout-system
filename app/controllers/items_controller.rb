class ItemsController < ApplicationController
  before_action :set_item, only: [:show, :edit, :update, :destroy, :confirm]

  def index
    @items = Item.all
  end

  def new
    @item = Item.new
  end

  def show
  end

  def edit
  end

  def create
    params[:item][:remaining_quantity] = params[:item][:quantity]
    @item = Item.new(item_params)
    if @item.save
      redirect_to :root, notice: 'Item was successfully created.'
    else
      render :new
    end
  end

  def confirm
  end

  def import
    begin
      Item.import(params[:file])
      redirect_to :root, notice: 'Items successfully created from CSV.'
    rescue
      redirect_to :root, alert: 'Invalid CSV format.'
    end
  end

  def update
    if @item.update(item_params)
      redirect_to items_url, notice: 'Item was successfully updated.'
    else
      render :edit
    end
  end

  def destroy
    @item.destroy
    redirect_to items_url, notice: 'Item was successfully destroyed.'
  end

  private
  def set_item
    @item = Item.find(params[:id])
  end

  def item_params
    params.require(:item).permit(:name, :category, :quantity, :description, :remaining_quantity, :upc)
  end
end
